/**
 * ========================================
 * MODELO - PRODUCTO
 * ========================================
 *
 * Los modelos en Angular definen la estructura de datos
 * que usamos en la aplicación. Corresponden a las entidades del backend.
 *
 * TypeScript nos permite tipar fuertemente nuestros datos,
 * lo que mejora la autocompletación y detecta errores en compilación.
 */

// Interface define la forma de un objeto
export interface Producto {
  id?: number;              // Opcional porque al crear no tenemos ID
  nombre: string;
  descripcion?: string;
  precio: number;
  stock: number;
  activo: boolean;
  fechaCreacion?: string;   // ISO string desde el backend
  fechaActualizacion?: string;
}

// Interface para crear/actualizar (sin campos automáticos)
export interface ProductoRequest {
  nombre: string;
  descripcion?: string;
  precio: number;
  stock: number;
  activo?: boolean;
}

// Interface para filtros de búsqueda
export interface ProductoFiltro {
  nombre?: string;
  precioMin?: number;
  precioMax?: number;
}
