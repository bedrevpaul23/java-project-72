@import hexlet.code.model.Url

@param Url url
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

<form method="post" action="${"/urls/" + url.getId() + "/checks"}">
  <input type="submit" value="Запустить проверку">
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
  </tbody>
</table>
`,
flash = flash,
flashClass = flashClass
)
