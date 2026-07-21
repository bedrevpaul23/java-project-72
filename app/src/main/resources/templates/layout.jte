@import gg.jte.Content

@param Content content

<!doctype html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Анализатор страниц</title>
  <link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    rel="stylesheet"
  >
</head>
<body class="min-vh-100 d-flex flex-column">
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand" href="/">Анализатор страниц</a>
      <div class="navbar-nav">
        <a class="nav-link" href="/urls">Сайты</a>
      </div>
    </div>
  </nav>
  <main class="container flex-grow-1 py-3">
    <div class="my-3"></div>
    ${content}
  </main>
  <footer class="border-top py-3">
    <div class="container text-center">
      <a href="https://ru.hexlet.io/">Hexlet</a>
    </div>
  </footer>
</body>
</html>
