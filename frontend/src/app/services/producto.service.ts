import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto, ProductoRequest, ProductoFiltro } from '../models/producto.model';

/**
 * ========================================
 * SERVICIO - PRODUCTO
 * ========================================
 *
 * Los servicios en Angular contienen la lógica de negocio y
 * las llamadas HTTP al backend. Son singletons inyectables.
 *
 * @Injectable({ providedIn: 'root' }) hace que:
 * - Angular cree una única instancia del servicio
 * - Esté disponible en toda la aplicación
 * - Se inyecte automáticamente donde se necesite
 */
@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  // URL base del backend
  // En producción, esto vendría de environment.ts
  private apiUrl = 'http://localhost:8080/api/productos';

  /**
   * Inyección de dependencias
   * HttpClient es el servicio de Angular para hacer peticiones HTTP
   */
  constructor(private http: HttpClient) { }

  /**
   * ========================================
   * OPERACIONES CRUD
   * ========================================
   *
   * Observables:
   * Los métodos de HttpClient retornan Observables.
   * Un Observable es como una promesa pero más potente:
   * - Puede emitir múltiples valores
   * - Se puede cancelar
   * - Tiene operadores de transformación
   */

  /**
   * Obtener todos los productos
   * GET /api/productos
   */
  obtenerTodos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }

  /**
   * Obtener producto por ID
   * GET /api/productos/{id}
   */
  obtenerPorId(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crear nuevo producto
   * POST /api/productos
   */
  crear(producto: ProductoRequest): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  /**
   * Actualizar producto
   * PUT /api/productos/{id}
   */
  actualizar(id: number, producto: ProductoRequest): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }

  /**
   * Eliminar producto
   * DELETE /api/productos/{id}
   */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * ========================================
   * OPERACIONES DE BÚSQUEDA
   * ========================================
   */

  /**
   * Buscar por nombre
   * GET /api/productos/buscar?nombre=texto
   */
  buscarPorNombre(nombre: string): Observable<Producto[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<Producto[]>(`${this.apiUrl}/buscar`, { params });
  }

  /**
   * Obtener productos activos
   * GET /api/productos/activos
   */
  obtenerActivos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/activos`);
  }

  /**
   * Buscar por rango de precio
   * GET /api/productos/precio?min=10&max=100
   */
  buscarPorPrecio(min: number, max: number): Observable<Producto[]> {
    const params = new HttpParams()
      .set('min', min.toString())
      .set('max', max.toString());
    return this.http.get<Producto[]>(`${this.apiUrl}/precio`, { params });
  }

  /**
   * Búsqueda con filtros múltiples
   * GET /api/productos/filtrar?nombre=x&precioMin=y&precioMax=z
   */
  buscarConFiltros(filtro: ProductoFiltro): Observable<Producto[]> {
    let params = new HttpParams();

    if (filtro.nombre) {
      params = params.set('nombre', filtro.nombre);
    }
    if (filtro.precioMin !== undefined) {
      params = params.set('precioMin', filtro.precioMin.toString());
    }
    if (filtro.precioMax !== undefined) {
      params = params.set('precioMax', filtro.precioMax.toString());
    }

    return this.http.get<Producto[]>(`${this.apiUrl}/filtrar`, { params });
  }

  /**
   * ========================================
   * OPERACIONES ESPECIALES
   * ========================================
   */

  /**
   * Actualizar stock
   * PATCH /api/productos/{id}/stock?stock=50
   */
  actualizarStock(id: number, stock: number): Observable<Producto> {
    const params = new HttpParams().set('stock', stock.toString());
    return this.http.patch<Producto>(`${this.apiUrl}/${id}/stock`, null, { params });
  }

  /**
   * Desactivar producto
   * PATCH /api/productos/{id}/desactivar
   */
  desactivar(id: number): Observable<Producto> {
    return this.http.patch<Producto>(`${this.apiUrl}/${id}/desactivar`, null);
  }
}
