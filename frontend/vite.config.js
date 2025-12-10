import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/worker': 'http://localhost:8080',
      '/leave': 'http://localhost:8080',
    },
  },
});
