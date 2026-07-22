@import java.util.List
@import hexlet.code.model.Url
@import hexlet.code.model.UrlCheck

@param Url url
@param List<UrlCheck> checks
@param String flash
@param String flashClass

@template.layout(
content = @`
<h1>Сайт: ${url.getName()}</h1>

<table class="table table-bordered" data-test="url">
  <tbody>
    <tr>
      <td>ID</td>
      <td>${url.getId()}</td>
    </tr>
    <tr>
      <td>Имя</td>
      <td>${url.getName()}</td>
    </tr>
    <tr>
      <td>Дата создания</td>
      <td>${url.getCreatedAtAsDate()}</td>
    </tr>
  </tbody>
</table>

<h2>Проверки</h2>

<form method="post" action="${"/urls/" + url.getId() + "/checks"}">
  <input class="btn btn-primary" type="submit" value="Запустить проверку">
</form>

<table class="table table-bordered table-hover" data-test="checks">
  <thead>
    <tr>
      <th>ID</th>
      <th>Код ответа</th>
      <th>h1</th>
      <th>title</th>
      <th>description</th>
      <th>Дата создания</th>
    </tr>
  </thead>
  <tbody>
    @for (UrlCheck check : checks)
      <tr>
        <td>${check.getId()}</td>
        <td>${check.getStatusCode()}</td>
        <td>${check.getH1ForDisplay()}</td>
        <td>${check.getTitleForDisplay()}</td>
        <td>${check.getDescriptionForDisplay()}</td>
        <td>${check.getCreatedAtAsDate()}</td>
      </tr>
    @endfor
  </tbody>
</table>
`,
flash = flash,
flashClass = flashClass
)
