import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Producto, Categoria } from '../../models/producto.model';
import { ProductoService } from '../../services/producto.service';
import { CategoriaService } from '../../services/categoria.service';
import { CarritoService } from '../../services/carrito.service';

/**
 * Componente para mostrar la lista de productos
 *
 * CONCEPTOS:
 * - OnInit: Lifecycle hook que se ejecuta al inicializar
 * - subscribe(): Se suscribe al Observable para recibir datos
 * - *ngFor: Directiva para iterar arrays en el template
 * - *ngIf: Directiva para renderizado condicional
 * - [(ngModel)]: Two-way data binding para formularios
 */
@Component({
  selector: 'app-producto-lista',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="productos-container">
      <!-- Filtros -->
      <div class="filtros">
        <h2><i class="fas fa-filter"></i> Filtrar Productos</h2>

        <div class="filtros-grid">
          <!-- Búsqueda por nombre -->
          <div class="filtro-item">
            <label for="busqueda">Buscar:</label>
            <input
              type="text"
              id="busqueda"
              [(ngModel)]="filtroNombre"
              (input)="aplicarFiltros()"
              placeholder="Nombre del producto...">
          </div>

          <!-- Filtro por categoría -->
          <div class="filtro-item">
            <label for="categoria">Categoría:</label>
            <select
              id="categoria"
              [(ngModel)]="filtroCategoriaId"
              (change)="aplicarFiltros()">
              <option [ngValue]="null">Todas</option>
              <option *ngFor="let cat of categorias" [ngValue]="cat.id">
                {{ cat.nombre }}
              </option>
            </select>
          </div>

          <!-- Filtro por precio máximo -->
          <div class="filtro-item">
            <label for="precio">Precio máximo:</label>
            <input
              type="number"
              id="precio"
              [(ngModel)]="filtroPrecioMax"
              (input)="aplicarFiltros()"
              placeholder="S/ máximo">
          </div>
        </div>
      </div>

      <!-- Loading -->
      <div class="loading" *ngIf="cargando">
        <div class="spinner"></div>
      </div>

      <!-- Mensaje de error -->
      <div class="alert alert-error" *ngIf="error">
        <i class="fas fa-exclamation-circle"></i> {{ error }}
      </div>

      <!-- Grid de productos -->
      <div class="productos-grid" *ngIf="!cargando && !error">
        <div class="card producto-card" *ngFor="let producto of productos">
          <!-- Imagen del producto -->
          <div class="producto-imagen">
            <img [src]="producto.imagen || 'https://via.placeholder.com/300x200'"
                 [alt]="producto.nombre">
            <span class="badge badge-success" *ngIf="producto.destacado">
              Destacado
            </span>
          </div>

          <!-- Info del producto -->
          <div class="producto-info">
            <span class="categoria-tag" *ngIf="producto.categoria">
              {{ producto.categoria.nombre }}
            </span>

            <h3>{{ producto.nombre }}</h3>

            <p class="descripcion">{{ producto.descripcion }}</p>

            <div class="producto-footer">
              <div class="precio-container">
                <span class="precio">S/ {{ producto.precio.toFixed(2) }}</span>
                <span class="precio-small">/ {{ producto.unidad }}</span>
              </div>

              <div class="acciones">
                <a [routerLink]="['/productos', producto.id]" class="btn btn-secondary">
                  <i class="fas fa-eye"></i>
                </a>
                <button class="btn btn-primary" (click)="agregarAlCarrito(producto)">
                  <i class="fas fa-cart-plus"></i>
                </button>
              </div>
            </div>

            <div class="stock-info">
              <span [class]="producto.stock > 10 ? 'stock-ok' : 'stock-bajo'">
                {{ producto.stock > 0 ? producto.stock + ' disponibles' : 'Agotado' }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Sin resultados -->
      <div class="sin-resultados" *ngIf="!cargando && productos.length === 0 && !error">
        <i class="fas fa-search"></i>
        <p>No se encontraron productos</p>
      </div>
    </div>
  `,
  styles: [`
    .productos-container {
      padding: 20px 0;
    }

    .filtros {
      background: white;
      padding: 20px;
      border-radius: 12px;
      margin-bottom: 30px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .filtros h2 {
      font-size: 18px;
      margin-bottom: 16px;
      color: var(--text-primary);
    }

    .filtros-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 16px;
    }

    .filtro-item {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .filtro-item label {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-secondary);
    }

    .producto-card {
      display: flex;
      flex-direction: column;
    }

    .producto-imagen {
      position: relative;
      height: 200px;
      overflow: hidden;
    }

    .producto-imagen img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .producto-imagen .badge {
      position: absolute;
      top: 10px;
      right: 10px;
    }

    .producto-info {
      padding: 16px;
      flex: 1;
      display: flex;
      flex-direction: column;
    }

    .categoria-tag {
      font-size: 12px;
      color: var(--primary-color);
      font-weight: 500;
      margin-bottom: 8px;
    }

    .producto-info h3 {
      font-size: 18px;
      margin-bottom: 8px;
    }

    .descripcion {
      font-size: 14px;
      color: var(--text-secondary);
      margin-bottom: 16px;
      flex: 1;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .producto-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
    }

    .precio-container {
      display: flex;
      align-items: baseline;
      gap: 4px;
    }

    .acciones {
      display: flex;
      gap: 8px;
    }

    .acciones .btn {
      padding: 8px 12px;
    }

    .stock-info {
      font-size: 12px;
    }

    .stock-ok {
      color: var(--success-color);
    }

    .stock-bajo {
      color: var(--warning-color);
    }

    .sin-resultados {
      text-align: center;
      padding: 60px 20px;
      color: var(--text-secondary);
    }

    .sin-resultados i {
      font-size: 48px;
      margin-bottom: 16px;
    }
  `]
})
export class ProductoListaComponent implements OnInit {
  productos: Producto[] = [];
  categorias: Categoria[] = [];
  cargando = false;
  error = '';

  // Filtros
  filtroNombre = '';
  filtroCategoriaId: number | null = null;
  filtroPrecioMax: number | null = null;

  constructor(
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private carritoService: CarritoService
  ) {}

  /**
   * ngOnInit se ejecuta cuando el componente se inicializa
   */
  ngOnInit(): void {
    this.cargarCategorias();
    this.cargarProductos();
  }

  cargarCategorias(): void {
    this.categoriaService.getActivas().subscribe({
      next: (data) => {
        this.categorias = data;
      },
      error: (err) => {
        console.error('Error al cargar categorías:', err);
      }
    });
  }

  cargarProductos(): void {
    this.cargando = true;
    this.error = '';

    this.productoService.getDisponibles().subscribe({
      next: (data) => {
        this.productos = data;
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'Error al cargar productos. ¿Está el backend ejecutándose?';
        this.cargando = false;
        console.error('Error:', err);
      }
    });
  }

  aplicarFiltros(): void {
    this.cargando = true;
    this.error = '';

    this.productoService.filtrar(
      this.filtroNombre || undefined,
      this.filtroCategoriaId || undefined,
      this.filtroPrecioMax || undefined
    ).subscribe({
      next: (data) => {
        this.productos = data;
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'Error al filtrar productos';
        this.cargando = false;
        console.error('Error:', err);
      }
    });
  }

  agregarAlCarrito(producto: Producto): void {
    this.carritoService.agregar(producto);
    // Aquí podrías mostrar una notificación
    console.log(`${producto.nombre} agregado al carrito`);
  }
}
