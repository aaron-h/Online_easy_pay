<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style>
:root {
  --primary-color: #00d4ff;
  --primary-dark: #0072ff;
  --primary-light: #66ffff;
  --bg-color: #002b36;
  --bg-light: #003a4a;
  --bg-lighter: #004a5a;
  --text-color: #ffffff;
  --text-secondary: #a0e0ff;
  --border-color: #00d4ff;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: var(--text-color);
  background-color: var(--bg-color);
}

#app {
  min-height: 100vh;
}
</style>