package hexlet.code.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;

public final class UrlController {
    private UrlController() {
    }

    public static void root(Context ctx) {
        ctx.render("index.jte", getPageData(ctx));
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = Objects.toString(ctx.formParam("url"), "").trim();
        URI parsedUrl;

        try {
            parsedUrl = new URI(inputUrl);
        } catch (URISyntaxException exception) {
            renderInvalidUrl(ctx);
            return;
        }

        var normalizedName = normalizeUrl(parsedUrl);

        if (normalizedName == null) {
            renderInvalidUrl(ctx);
            return;
        }

        var existingUrl = UrlRepository.findByName(normalizedName);

        if (existingUrl.isPresent()) {
            var url = existingUrl.get();
            setFlash(
                    ctx,
                    "Страница уже существует",
                    "alert alert-info"
            );
            ctx.redirect("/urls/" + url.getId());
            return;
        }

        var url = new Url(normalizedName);
        UrlRepository.save(url);

        setFlash(
                ctx,
                "Страница успешно добавлена",
                "alert alert-success"
        );
        ctx.redirect("/urls/" + url.getId());
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var latestChecks = UrlCheckRepository.findLatestChecks();
        var latestCheckDates = new HashMap<Long, String>();
        var latestCheckStatusCodes = new HashMap<Long, String>();

        for (var url : urls) {
            var latestCheck = latestChecks.get(url.getId());

            if (latestCheck != null) {
                latestCheckDates.put(
                        url.getId(),
                        latestCheck.getCreatedAtAsDate()
                );
                latestCheckStatusCodes.put(
                        url.getId(),
                        String.valueOf(latestCheck.getStatusCode())
                );
            }
        }

        var pageData = getPageData(ctx);
        pageData.put("urls", urls);
        pageData.put("latestCheckDates", latestCheckDates);
        pageData.put(
                "latestCheckStatusCodes",
                latestCheckStatusCodes
        );

        ctx.render("urls/index.jte", pageData);
    }

    public static void show(Context ctx) throws SQLException {
        var id = Long.valueOf(ctx.pathParam("id"));
        var url = UrlRepository.find(id)
                .orElseThrow(
                        () -> new NotFoundResponse("Url not found")
                );

        var pageData = getPageData(ctx);
        pageData.put("url", url);
        pageData.put(
                "checks",
                UrlCheckRepository.findByUrlId(id)
        );

        ctx.render("urls/show.jte", pageData);
    }

    public static void createCheck(Context ctx) throws SQLException {
        var id = Long.valueOf(ctx.pathParam("id"));
        var url = UrlRepository.find(id)
                .orElseThrow(
                        () -> new NotFoundResponse("Url not found")
                );

        try {
            var urlCheck = checkUrl(url);
            UrlCheckRepository.save(urlCheck);

            setFlash(
                    ctx,
                    "Страница успешно проверена",
                    "alert alert-success"
            );
        } catch (RuntimeException exception) {
            setFlash(
                    ctx,
                    "Произошла ошибка при проверке",
                    "alert alert-danger"
            );
        }

        ctx.redirect("/urls/" + id);
    }

    public static String normalizeUrl(
            String input
    ) throws URISyntaxException {
        if (input == null || input.isBlank()) {
            throw new URISyntaxException(
                    Objects.toString(input, ""),
                    "Invalid URL"
            );
        }

        var normalizedUrl = normalizeUrl(new URI(input.trim()));

        if (normalizedUrl == null) {
            throw new URISyntaxException(input, "Invalid URL");
        }

        return normalizedUrl;
    }

    private static String normalizeUrl(URI parsedUrl) {
        var scheme = parsedUrl.getScheme();
        var host = parsedUrl.getHost();

        var isSupportedScheme = "http".equalsIgnoreCase(scheme)
                || "https".equalsIgnoreCase(scheme);

        if (!isSupportedScheme
                || Objects.toString(host, "").isBlank()) {
            return null;
        }

        var normalizedUrl = scheme + "://" + host;
        var port = parsedUrl.getPort();

        return port == -1
                ? normalizedUrl
                : normalizedUrl + ":" + port;
    }

    private static UrlCheck checkUrl(Url url) {
        var response = Unirest.get(url.getName()).asString();

        if (response.getStatus() >= 400) {
            throw new IllegalStateException(
                    "URL check failed with status "
                            + response.getStatus()
            );
        }

        var body = Objects.toString(response.getBody(), "");
        var document = Jsoup.parse(body);

        var h1Element = document.selectFirst("h1");
        var h1 = h1Element == null
                ? ""
                : h1Element.text();

        var descriptionElement =
                document.selectFirst("meta[name=description]");
        var description = descriptionElement == null
                ? ""
                : descriptionElement.attr("content");

        return new UrlCheck(
                url.getId(),
                response.getStatus(),
                h1,
                document.title(),
                description
        );
    }

    private static Map<String, Object> getPageData(Context ctx) {
        return getPageData(
                ctx.consumeSessionAttribute("flash"),
                ctx.consumeSessionAttribute("flashClass")
        );
    }

    private static Map<String, Object> getPageData(
            String flash,
            String flashClass
    ) {
        var pageData = new HashMap<String, Object>();
        pageData.put("flash", flash);
        pageData.put("flashClass", flashClass);
        return pageData;
    }

    private static void setFlash(
            Context ctx,
            String flash,
            String flashClass
    ) {
        ctx.sessionAttribute("flash", flash);
        ctx.sessionAttribute("flashClass", flashClass);
    }

    private static void renderInvalidUrl(Context ctx) {
        ctx.status(422).render(
                "index.jte",
                getPageData(
                        "Некорректный URL",
                        "alert alert-danger"
                )
        );
    }
}
