import { Routes } from '@angular/router';
import { ProductoListaComponent } from './components/producto-lista/producto-lista.component';
import { ProductoDetalleComponent } from './components/producto-detalle/producto-detalle.component';
import { CarritoComponent } from './components/carrito/carrito.component';

/**
 * Definición de rutas de la aplicación
 *
 * CONCEPTOS:
 * - path: URL de la ruta
 * - component: Componente que se muestra en esa ruta
 * - redirectTo: Redirecciona a otra ruta
 * - pathMatch: 'full' requiere coincidencia exacta
 * - :id: Parámetro dinámico en la URL
 */
export const routes: Routes = [
  // Ruta por defecto -> redirige a productos
  { path: '', redirectTo: '/productos', pathMatch: 'full' },

  // Lista de productos
  { path: 'productos', component: ProductoListaComponent },

  // Detalle de producto (con parámetro id)
  { path: 'productos/:id', component: ProductoDetalleComponent },

  // Carrito de compras
  { path: 'carrito', component: CarritoComponent },

  // Ruta no encontrada -> redirige a productos
  { path: '**', redirectTo: '/productos' }
];
