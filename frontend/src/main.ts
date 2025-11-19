/**
 * Punto de entrada de la aplicación Angular
 *
 * bootstrapApplication: Inicia la aplicación con un componente raíz
 * appConfig: Configuración de la aplicación (providers, rutas, etc.)
 */
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
