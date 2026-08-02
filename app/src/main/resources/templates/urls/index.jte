@import java.util.List
@import java.util.Map
@import hexlet.code.model.Url
@import hexlet.code.model.UrlCheck
@import hexlet.code.util.FormatUtils

@param List<Url> urls
@param Map<Long, UrlCheck> latestChecks
@param String flash
@param String flashStatus

@template.layout(
content = @`
<h1>Сайты</h1>

<table class="table table-bordered table-hover" data-test="urls">
  <thead>
    <tr>
      <th>ID</th>
      <th>Имя</th>
      <th>Последняя проверка</th>
      <th>Код ответа</th>
    </tr>
  </thead>
  <tbody>
    @for (Url url : urls)
      <tr>
        <td>${url.getId()}</td>
        <td><a href="${"/urls/" + url.getId()}">${url.getName()}</a></td>
        <td>
          @if (latestChecks.containsKey(url.getId()))
            ${FormatUtils.formatDateTime(latestChecks.get(url.getId()).getCreatedAt())}
          @endif
        </td>
        <td>
          @if (latestChecks.containsKey(url.getId()))
            ${latestChecks.get(url.getId()).getStatusCode()}
          @endif
        </td>
      </tr>
    @endfor
  </tbody>
</table>
`,
flash = flash,
flashStatus = flashStatus
)
