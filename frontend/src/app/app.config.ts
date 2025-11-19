import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app.routes';

/**
 * Configuración de la aplicación Angular
 *
 * CONCEPTOS:
 * - providers: Servicios disponibles en toda la aplicación
 * - provideRouter: Configura el sistema de rutas
 * - provideHttpClient: Habilita HttpClient para peticiones HTTP
 */
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient()
  ]
};
