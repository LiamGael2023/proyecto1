import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto, PaginatedResponse } from '../models/producto.model';

/**
 * Servicio para comunicarse con el API de productos
 *
 * CONCEPTOS ANGULAR:
 * - @Injectable: Marca la clase como un servicio inyectable
 * - providedIn: 'root': Singleton disponible en toda la app
 * - HttpClient: Cliente HTTP para hacer peticiones
 * - Observable: Flujo de datos asíncrono (patrón reactivo)
 *
 * PATRÓN:
 * Component -> Service -> HttpClient -> Backend API
 */
@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  // URL base del API (apunta al backend Spring Boot)
  private apiUrl = 'http://localhost:8080/api/productos';

  /**
   * Inyección de dependencias por constructor
   * HttpClient viene del módulo HttpClientModule
   */
  constructor(private http: HttpClient) { }

  // ========== MÉTODOS GET ==========

  /**
   * Obtiene todos los productos
   * GET /api/productos
   */
  getProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }

  /**
   * Obtiene un producto por ID
   * GET /api/productos/{id}
   */
  getProductoById(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtiene productos disponibles
   * GET /api/productos/disponibles
   */
  getDisponibles(): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/disponibles`);
  }

  /**
   * Obtiene productos destacados
   * GET /api/productos/destacados
   */
  getDestacados(): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/destacados`);
  }

  /**
   * Obtiene productos por categoría
   * GET /api/productos/categoria/{categoriaId}
   */
  getByCategoria(categoriaId: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/categoria/${categoriaId}`);
  }

  /**
   * Busca productos por nombre
   * GET /api/productos/buscar?nombre=xxx
   */
  buscar(nombre: string): Observable<Producto[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<Producto[]>(`${this.apiUrl}/buscar`, { params });
  }

  /**
   * Búsqueda avanzada con filtros
   * GET /api/productos/filtrar?nombre=xxx&categoriaId=1&precioMax=100
   *
   * HttpParams permite construir query strings de forma segura
   */
  filtrar(nombre?: string, categoriaId?: number, precioMax?: number): Observable<Producto[]> {
    let params = new HttpParams();

    if (nombre) {
      params = params.set('nombre', nombre);
    }
    if (categoriaId) {
      params = params.set('categoriaId', categoriaId.toString());
    }
    if (precioMax) {
      params = params.set('precioMax', precioMax.toString());
    }

    return this.http.get<Producto[]>(`${this.apiUrl}/filtrar`, { params });
  }

  /**
   * Obtiene productos paginados
   * GET /api/productos/paginado?page=0&size=10&sortBy=precio&sortDir=asc
   */
  getPaginados(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    sortDir: string = 'asc'
  ): Observable<PaginatedResponse<Producto>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PaginatedResponse<Producto>>(
      `${this.apiUrl}/paginado`,
      { params }
    );
  }

  // ========== MÉTODOS POST ==========

  /**
   * Crea un nuevo producto
   * POST /api/productos
   */
  crear(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  // ========== MÉTODOS PUT ==========

  /**
   * Actualiza un producto existente
   * PUT /api/productos/{id}
   */
  actualizar(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }

  // ========== MÉTODOS PATCH ==========

  /**
   * Actualiza el stock de un producto
   * PATCH /api/productos/{id}/stock?cantidad=5
   */
  actualizarStock(id: number, cantidad: number): Observable<Producto> {
    const params = new HttpParams().set('cantidad', cantidad.toString());
    return this.http.patch<Producto>(`${this.apiUrl}/${id}/stock`, null, { params });
  }

  // ========== MÉTODOS DELETE ==========

  /**
   * Elimina un producto
   * DELETE /api/productos/{id}
   */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
