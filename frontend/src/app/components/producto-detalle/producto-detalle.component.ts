import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Producto } from '../../models/producto.model';
import { ProductoService } from '../../services/producto.service';
import { CarritoService } from '../../services/carrito.service';

/**
 * Componente para mostrar el detalle de un producto
 *
 * CONCEPTOS:
 * - ActivatedRoute: Permite acceder a parámetros de la URL
 * - Router: Permite navegar programáticamente
 * - switchMap: Operador RxJS para encadenar observables
 */
@Component({
  selector: 'app-producto-detalle',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <div class="detalle-container" *ngIf="producto">
      <!-- Breadcrumb -->
      <nav class="breadcrumb">
        <a routerLink="/productos">
          <i class="fas fa-arrow-left"></i> Volver a productos
        </a>
      </nav>

      <div class="detalle-grid">
        <!-- Imagen -->
        <div class="detalle-imagen">
          <img [src]="producto.imagen || 'https://via.placeholder.com/500x400'"
               [alt]="producto.nombre">
        </div>

        <!-- Info -->
        <div class="detalle-info">
          <span class="categoria-tag" *ngIf="producto.categoria">
            {{ producto.categoria.nombre }}
          </span>

          <h1>{{ producto.nombre }}</h1>

          <p class="descripcion">{{ producto.descripcion }}</p>

          <div class="precio-section">
            <span class="precio">S/ {{ producto.precio.toFixed(2) }}</span>
            <span class="unidad">/ {{ producto.unidad }}</span>
          </div>

          <!-- Stock -->
          <div class="stock-section">
            <span [class]="producto.stock > 10 ? 'stock-ok' : 'stock-bajo'">
              <i class="fas fa-box"></i>
              {{ producto.stock }} unidades disponibles
            </span>
          </div>

          <!-- Cantidad y agregar -->
          <div class="compra-section">
            <div class="cantidad-control">
              <button (click)="decrementarCantidad()" [disabled]="cantidad <= 1">
                <i class="fas fa-minus"></i>
              </button>
              <input type="number" [(ngModel)]="cantidad" min="1" [max]="producto.stock">
              <button (click)="incrementarCantidad()" [disabled]="cantidad >= producto.stock">
                <i class="fas fa-plus"></i>
              </button>
            </div>

            <button class="btn btn-primary btn-lg"
                    (click)="agregarAlCarrito()"
                    [disabled]="producto.stock === 0">
              <i class="fas fa-cart-plus"></i>
              Agregar al carrito
            </button>
          </div>

          <!-- Info adicional -->
          <div class="info-adicional">
            <div class="info-item" *ngIf="producto.fechaCreacion">
              <i class="fas fa-calendar"></i>
              <span>Publicado: {{ producto.fechaCreacion | date:'dd/MM/yyyy' }}</span>
            </div>
            <div class="info-item" *ngIf="producto.destacado">
              <i class="fas fa-star"></i>
              <span>Producto destacado</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Loading -->
    <div class="loading" *ngIf="cargando">
      <div class="spinner"></div>
    </div>

    <!-- Error -->
    <div class="alert alert-error" *ngIf="error">
      <i class="fas fa-exclamation-circle"></i> {{ error }}
      <br>
      <a routerLink="/productos" class="btn btn-secondary" style="margin-top: 16px;">
        Volver a productos
      </a>
    </div>
  `,
  styles: [`
    .detalle-container {
      padding: 20px 0;
    }

    .breadcrumb {
      margin-bottom: 24px;
    }

    .breadcrumb a {
      color: var(--primary-color);
      text-decoration: none;
      font-weight: 500;
    }

    .breadcrumb a:hover {
      text-decoration: underline;
    }

    .detalle-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 40px;
    }

    @media (max-width: 768px) {
      .detalle-grid {
        grid-template-columns: 1fr;
      }
    }

    .detalle-imagen {
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    }

    .detalle-imagen img {
      width: 100%;
      height: auto;
    }

    .detalle-info {
      padding: 20px 0;
    }

    .categoria-tag {
      display: inline-block;
      background: var(--primary-light);
      color: white;
      padding: 4px 12px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 500;
      margin-bottom: 16px;
    }

    .detalle-info h1 {
      font-size: 32px;
      margin-bottom: 16px;
    }

    .descripcion {
      color: var(--text-secondary);
      line-height: 1.8;
      margin-bottom: 24px;
    }

    .precio-section {
      margin-bottom: 16px;
    }

    .precio-section .precio {
      font-size: 36px;
    }

    .precio-section .unidad {
      font-size: 16px;
      color: var(--text-secondary);
    }

    .stock-section {
      margin-bottom: 24px;
    }

    .stock-ok {
      color: var(--success-color);
    }

    .stock-bajo {
      color: var(--warning-color);
    }

    .compra-section {
      display: flex;
      gap: 16px;
      align-items: center;
      margin-bottom: 32px;
    }

    .cantidad-control {
      display: flex;
      align-items: center;
      border: 1px solid var(--border-color);
      border-radius: 8px;
      overflow: hidden;
    }

    .cantidad-control button {
      padding: 12px 16px;
      border: none;
      background: var(--background-color);
      cursor: pointer;
      transition: background 0.3s;
    }

    .cantidad-control button:hover:not(:disabled) {
      background: var(--border-color);
    }

    .cantidad-control button:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .cantidad-control input {
      width: 60px;
      text-align: center;
      border: none;
      padding: 12px;
    }

    .btn-lg {
      padding: 14px 28px;
      font-size: 16px;
    }

    .info-adicional {
      border-top: 1px solid var(--border-color);
      padding-top: 24px;
    }

    .info-item {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;
      color: var(--text-secondary);
      font-size: 14px;
    }

    .info-item i {
      color: var(--primary-color);
    }
  `]
})
export class ProductoDetalleComponent implements OnInit {
  producto: Producto | null = null;
  cantidad = 1;
  cargando = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productoService: ProductoService,
    private carritoService: CarritoService
  ) {}

  ngOnInit(): void {
    // Obtener el ID de la URL
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarProducto(+id);
    }
  }

  cargarProducto(id: number): void {
    this.cargando = true;
    this.error = '';

    this.productoService.getProductoById(id).subscribe({
      next: (producto) => {
        this.producto = producto;
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'Producto no encontrado';
        this.cargando = false;
        console.error('Error:', err);
      }
    });
  }

  incrementarCantidad(): void {
    if (this.producto && this.cantidad < this.producto.stock) {
      this.cantidad++;
    }
  }

  decrementarCantidad(): void {
    if (this.cantidad > 1) {
      this.cantidad--;
    }
  }

  agregarAlCarrito(): void {
    if (this.producto) {
      this.carritoService.agregar(this.producto, this.cantidad);
      // Navegar al carrito
      this.router.navigate(['/carrito']);
    }
  }
}
