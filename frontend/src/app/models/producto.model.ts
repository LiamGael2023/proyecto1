/**
 * Interfaces TypeScript para los modelos de datos
 *
 * CONCEPTOS TYPESCRIPT:
 * - interface: Define la estructura de un objeto
 * - ?: Campo opcional
 * - Los tipos ayudan a prevenir errores y dan autocompletado
 */

// Modelo de Categoría
export interface Categoria {
  id: number;
  nombre: string;
  descripcion?: string;
  imagen?: string;
  activo: boolean;
}

// Modelo de Producto
export interface Producto {
  id: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  stock: number;
  imagen?: string;
  unidad: string;
  disponible: boolean;
  destacado: boolean;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  categoria?: Categoria;
}

// Modelo para el carrito
export interface ItemCarrito {
  producto: Producto;
  cantidad: number;
}

// Modelo para respuestas paginadas
export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
