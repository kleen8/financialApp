import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'

// https://vite.dev/config/
export default defineConfig({
  plugins: [svelte()],
  server : {
      proxy : {
          '/api' : "http://backend:8080" // back-end on port 8080
      }
  }
})
