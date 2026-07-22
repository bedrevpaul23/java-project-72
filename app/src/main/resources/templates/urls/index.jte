@import java.util.List
@import hexlet.code.model.Url

@param List<Url> urls
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
      <th>Дата создания</th>
      <th>Код ответа</th>
    </tr>
  </thead>
  <tbody>
    @for (Url url : urls)
      <tr>
        <td>${url.getId()}</td>
        <td><a href="${"/urls/" + url.getId()}">${url.getName()}</a></td>
        <td>${url.getCreatedAtAsDate()}</td>
        <td></td>
      </tr>
    @endfor
  </tbody>
</table>
`,
flash = flash,
flashClass = flashClass
)
