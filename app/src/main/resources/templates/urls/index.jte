@import java.util.List
@import java.util.Map
@import hexlet.code.model.Url

@param List<Url> urls
@param Map<Long, String> latestCheckDates
@param Map<Long, String> latestCheckStatusCodes
@param String flash
@param String flashClass

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
          @if (latestCheckDates.containsKey(url.getId()))
            ${latestCheckDates.get(url.getId())}
          @endif
        </td>
        <td>
          @if (latestCheckStatusCodes.containsKey(url.getId()))
            ${latestCheckStatusCodes.get(url.getId())}
          @endif
        </td>
      </tr>
    @endfor
  </tbody>
</table>
`,
flash = flash,
flashClass = flashClass
)
