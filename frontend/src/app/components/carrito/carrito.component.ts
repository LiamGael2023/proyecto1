import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ItemCarrito } from '../../models/producto.model';
import { CarritoService } from '../../services/carrito.service';

/**
 * Componente para mostrar el carrito de compras
 */
@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="carrito-container">
      <h1><i class="fas fa-shopping-cart"></i> Tu Carrito</h1>

      <!-- Carrito vacío -->
      <div class="carrito-vacio" *ngIf="items.length === 0">
        <i class="fas fa-shopping-basket"></i>
        <p>Tu carrito está vacío</p>
        <a routerLink="/productos" class="btn btn-primary">
          <i class="fas fa-store"></i> Ver productos
        </a>
      </div>

      <!-- Items del carrito -->
      <div class="carrito-content" *ngIf="items.length > 0">
        <div class="carrito-items">
          <div class="carrito-item" *ngFor="let item of items">
            <!-- Imagen -->
            <div class="item-imagen">
              <img [src]="item.producto.imagen || 'https://via.placeholder.com/100'"
                   [alt]="item.producto.nombre">
            </div>

            <!-- Info -->
            <div class="item-info">
              <h3>{{ item.producto.nombre }}</h3>
              <p class="item-categoria" *ngIf="item.producto.categoria">
                {{ item.producto.categoria.nombre }}
              </p>
              <p class="item-precio">
                S/ {{ item.producto.precio.toFixed(2) }} / {{ item.producto.unidad }}
              </p>
            </div>

            <!-- Cantidad -->
            <div class="item-cantidad">
              <button (click)="decrementar(item)" [disabled]="item.cantidad <= 1">
                <i class="fas fa-minus"></i>
              </button>
              <span>{{ item.cantidad }}</span>
              <button (click)="incrementar(item)" [disabled]="item.cantidad >= item.producto.stock">
                <i class="fas fa-plus"></i>
              </button>
            </div>

            <!-- Subtotal -->
            <div class="item-subtotal">
              <span class="precio">
                S/ {{ (item.producto.precio * item.cantidad).toFixed(2) }}
              </span>
            </div>

            <!-- Eliminar -->
            <button class="btn-eliminar" (click)="eliminar(item)">
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </div>

        <!-- Resumen -->
        <div class="carrito-resumen">
          <h2>Resumen del pedido</h2>

          <div class="resumen-linea">
            <span>Subtotal ({{ totalItems }} items)</span>
            <span>S/ {{ total.toFixed(2) }}</span>
          </div>

          <div class="resumen-linea">
            <span>Envío</span>
            <span>Gratis</span>
          </div>

          <div class="resumen-linea total">
            <span>Total</span>
            <span class="precio">S/ {{ total.toFixed(2) }}</span>
          </div>

          <button class="btn btn-accent btn-block">
            <i class="fas fa-credit-card"></i> Proceder al pago
          </button>

          <button class="btn btn-secondary btn-block" (click)="vaciarCarrito()">
            <i class="fas fa-trash"></i> Vaciar carrito
          </button>

          <a routerLink="/productos" class="btn-continuar">
            <i class="fas fa-arrow-left"></i> Continuar comprando
          </a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .carrito-container {
      padding: 20px 0;
    }

    .carrito-container h1 {
      margin-bottom: 30px;
    }

    .carrito-vacio {
      text-align: center;
      padding: 60px 20px;
      background: white;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .carrito-vacio i {
      font-size: 64px;
      color: var(--text-secondary);
      margin-bottom: 20px;
    }

    .carrito-vacio p {
      color: var(--text-secondary);
      margin-bottom: 20px;
    }

    .carrito-content {
      display: grid;
      grid-template-columns: 1fr 350px;
      gap: 30px;
    }

    @media (max-width: 968px) {
      .carrito-content {
        grid-template-columns: 1fr;
      }
    }

    .carrito-items {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }

    .carrito-item {
      display: flex;
      align-items: center;
      gap: 16px;
      background: white;
      padding: 16px;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .item-imagen {
      width: 80px;
      height: 80px;
      border-radius: 8px;
      overflow: hidden;
      flex-shrink: 0;
    }

    .item-imagen img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .item-info {
      flex: 1;
    }

    .item-info h3 {
      font-size: 16px;
      margin-bottom: 4px;
    }

    .item-categoria {
      font-size: 12px;
      color: var(--primary-color);
      margin-bottom: 4px;
    }

    .item-precio {
      font-size: 14px;
      color: var(--text-secondary);
    }

    .item-cantidad {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .item-cantidad button {
      width: 30px;
      height: 30px;
      border: 1px solid var(--border-color);
      background: white;
      border-radius: 4px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .item-cantidad button:hover:not(:disabled) {
      background: var(--background-color);
    }

    .item-cantidad button:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .item-cantidad span {
      width: 30px;
      text-align: center;
      font-weight: 500;
    }

    .item-subtotal {
      min-width: 100px;
      text-align: right;
    }

    .btn-eliminar {
      background: none;
      border: none;
      color: var(--error-color);
      cursor: pointer;
      padding: 8px;
      border-radius: 4px;
      transition: background 0.3s;
    }

    .btn-eliminar:hover {
      background: #ffebee;
    }

    .carrito-resumen {
      background: white;
      padding: 24px;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      height: fit-content;
      position: sticky;
      top: 20px;
    }

    .carrito-resumen h2 {
      font-size: 18px;
      margin-bottom: 20px;
      padding-bottom: 16px;
      border-bottom: 1px solid var(--border-color);
    }

    .resumen-linea {
      display: flex;
      justify-content: space-between;
      margin-bottom: 12px;
      font-size: 14px;
    }

    .resumen-linea.total {
      font-size: 18px;
      font-weight: 600;
      margin-top: 16px;
      padding-top: 16px;
      border-top: 1px solid var(--border-color);
    }

    .btn-block {
      width: 100%;
      margin-top: 16px;
    }

    .btn-continuar {
      display: block;
      text-align: center;
      margin-top: 16px;
      color: var(--primary-color);
      text-decoration: none;
      font-size: 14px;
    }

    .btn-continuar:hover {
      text-decoration: underline;
    }
  `]
})
export class CarritoComponent implements OnInit {
  items: ItemCarrito[] = [];
  total = 0;
  totalItems = 0;

  constructor(private carritoService: CarritoService) {}

  ngOnInit(): void {
    // Suscribirse a cambios en el carrito
    this.carritoService.carrito$.subscribe(items => {
      this.items = items;
      this.calcularTotales();
    });
  }

  calcularTotales(): void {
    this.total = this.carritoService.getTotal();
    this.totalItems = this.carritoService.getTotalItems();
  }

  incrementar(item: ItemCarrito): void {
    this.carritoService.actualizarCantidad(
      item.producto.id,
      item.cantidad + 1
    );
  }

  decrementar(item: ItemCarrito): void {
    this.carritoService.actualizarCantidad(
      item.producto.id,
      item.cantidad - 1
    );
  }

  eliminar(item: ItemCarrito): void {
    this.carritoService.eliminar(item.producto.id);
  }

  vaciarCarrito(): void {
    if (confirm('¿Estás seguro de vaciar el carrito?')) {
      this.carritoService.vaciar();
    }
  }
}
