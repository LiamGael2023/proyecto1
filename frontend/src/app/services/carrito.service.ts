import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Producto, ItemCarrito } from '../models/producto.model';

/**
 * Servicio para manejar el carrito de compras
 *
 * CONCEPTOS RXJS:
 * - BehaviorSubject: Observable que guarda el último valor emitido
 * - Permite que múltiples componentes se suscriban y reciban actualizaciones
 */
@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  /**
   * BehaviorSubject para el estado del carrito
   * - Emite el valor actual a nuevos suscriptores
   * - Permite actualizar y notificar cambios
   */
  private carritoSubject = new BehaviorSubject<ItemCarrito[]>([]);

  // Observable público para que los componentes se suscriban
  carrito$: Observable<ItemCarrito[]> = this.carritoSubject.asObservable();

  constructor() {
    // Cargar carrito desde localStorage al iniciar
    this.cargarDesdeStorage();
  }

  /**
   * Agrega un producto al carrito
   */
  agregar(producto: Producto, cantidad: number = 1): void {
    const carritoActual = this.carritoSubject.getValue();
    const itemExistente = carritoActual.find(
      item => item.producto.id === producto.id
    );

    if (itemExistente) {
      // Si ya existe, aumentar cantidad
      itemExistente.cantidad += cantidad;
    } else {
      // Si no existe, agregar nuevo item
      carritoActual.push({ producto, cantidad });
    }

    this.actualizarCarrito(carritoActual);
  }

  /**
   * Elimina un producto del carrito
   */
  eliminar(productoId: number): void {
    const carritoActual = this.carritoSubject.getValue();
    const carritoFiltrado = carritoActual.filter(
      item => item.producto.id !== productoId
    );
    this.actualizarCarrito(carritoFiltrado);
  }

  /**
   * Actualiza la cantidad de un producto
   */
  actualizarCantidad(productoId: number, cantidad: number): void {
    const carritoActual = this.carritoSubject.getValue();
    const item = carritoActual.find(i => i.producto.id === productoId);

    if (item) {
      if (cantidad <= 0) {
        this.eliminar(productoId);
      } else {
        item.cantidad = cantidad;
        this.actualizarCarrito(carritoActual);
      }
    }
  }

  /**
   * Vacía el carrito
   */
  vaciar(): void {
    this.actualizarCarrito([]);
  }

  /**
   * Obtiene el total de items en el carrito
   */
  getTotalItems(): number {
    return this.carritoSubject.getValue()
      .reduce((total, item) => total + item.cantidad, 0);
  }

  /**
   * Obtiene el precio total del carrito
   */
  getTotal(): number {
    return this.carritoSubject.getValue()
      .reduce((total, item) => total + (item.producto.precio * item.cantidad), 0);
  }

  /**
   * Actualiza el carrito y guarda en localStorage
   */
  private actualizarCarrito(carrito: ItemCarrito[]): void {
    this.carritoSubject.next(carrito);
    this.guardarEnStorage(carrito);
  }

  /**
   * Guarda el carrito en localStorage
   */
  private guardarEnStorage(carrito: ItemCarrito[]): void {
    localStorage.setItem('agromarket_carrito', JSON.stringify(carrito));
  }

  /**
   * Carga el carrito desde localStorage
   */
  private cargarDesdeStorage(): void {
    const carritoGuardado = localStorage.getItem('agromarket_carrito');
    if (carritoGuardado) {
      try {
        const carrito = JSON.parse(carritoGuardado);
        this.carritoSubject.next(carrito);
      } catch (e) {
        console.error('Error al cargar carrito:', e);
      }
    }
  }
}
