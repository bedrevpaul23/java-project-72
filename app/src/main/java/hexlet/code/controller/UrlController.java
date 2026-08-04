package hexlet.code.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;

public final class UrlController {
    private UrlController() {
    }

    public static void root(Context ctx) {
        ctx.render("index.jte", getPageData(ctx));
    }

    public static void create(Context ctx) {
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
            setFlash(ctx, "Страница уже существует", "info");
            ctx.redirect("/urls/" + url.getId());
            return;
        }

        var url = new Url(normalizedName);
        UrlRepository.save(url);

        setFlash(ctx, "Страница успешно добавлена", "success");
        ctx.redirect("/urls/" + url.getId());
    }

    public static void index(Context ctx) {
        var urls = UrlRepository.getEntities();
        var latestChecks = UrlCheckRepository.findLatestChecks();

        var pageData = getPageData(ctx);
        pageData.put("urls", urls);
        pageData.put("latestChecks", latestChecks);

        ctx.render("urls/index.jte", pageData);
    }

    public static void show(Context ctx) {
        var id = Long.valueOf(ctx.pathParam("id"));
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        var pageData = getPageData(ctx);
        pageData.put("url", url);
        pageData.put("checks", UrlCheckRepository.findByUrlId(id));

        ctx.render("urls/show.jte", pageData);
    }

    public static void createCheck(Context ctx) {
        var id = Long.valueOf(ctx.pathParam("id"));
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        UrlCheck urlCheck;
        try {
            urlCheck = checkUrl(url);
        } catch (RuntimeException exception) {
            setFlash(ctx, "Произошла ошибка при проверке", "danger");
            ctx.redirect("/urls/" + id);
            return;
        }

        UrlCheckRepository.save(urlCheck);
        setFlash(ctx, "Страница успешно проверена", "success");
        ctx.redirect("/urls/" + id);
    }

    public static String normalizeUrl(String input) throws URISyntaxException {
        if (input == null || input.isBlank()) {
            throw new URISyntaxException(Objects.toString(input, ""), "Invalid URL");
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

        if (!isSupportedScheme || Objects.toString(host, "").isBlank()) {
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

        if (response.getStatus() >= HttpStatus.BAD_REQUEST.getCode()) {
            throw new IllegalStateException("URL check failed with status " + response.getStatus());
        }

        var body = Objects.toString(response.getBody(), "");
        var document = Jsoup.parse(body);

        var h1Element = document.selectFirst("h1");
        var h1 = h1Element == null
                ? ""
                : h1Element.text();

        var descriptionElement = document.selectFirst("meta[name=description]");
        var description = descriptionElement == null
                ? ""
                : descriptionElement.attr("content");

        return new UrlCheck(url.getId(), response.getStatus(), h1, document.title(), description);
    }

    private static Map<String, Object> getPageData(Context ctx) {
        return getPageData(ctx.consumeSessionAttribute("flash"), ctx.consumeSessionAttribute("flashStatus"));
    }

    private static Map<String, Object> getPageData(String flash, String flashStatus) {
        var pageData = new HashMap<String, Object>();
        pageData.put("flash", flash);
        pageData.put("flashStatus", flashStatus);
        return pageData;
    }

    private static void setFlash(Context ctx, String flash, String flashStatus) {
        ctx.sessionAttribute("flash", flash);
        ctx.sessionAttribute("flashStatus", flashStatus);
    }

    private static void renderInvalidUrl(Context ctx) {
        ctx.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .render("index.jte", getPageData("Некорректный URL", "danger"));
    }
}
