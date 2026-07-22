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
`,
flash = flash,
flashClass = flashClass
)
