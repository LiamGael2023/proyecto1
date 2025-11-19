# Guía de Aprendizaje: Angular

## ¿Qué es Angular?

Angular es un framework de desarrollo para construir aplicaciones web SPA (Single Page Applications). Desarrollado por Google.

## Conceptos Fundamentales

### 1. Componentes

Son los bloques de construcción de Angular. Cada componente tiene:
- Template (HTML)
- Clase (TypeScript)
- Estilos (CSS)

```typescript
@Component({
  selector: 'app-producto',
  template: '<h1>{{ titulo }}</h1>',
  styles: ['h1 { color: blue; }']
})
export class ProductoComponent {
  titulo = 'Mi Producto';
}
```

### 2. Módulos

Agrupan componentes, servicios y otros módulos relacionados.

```typescript
@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, HttpClientModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

### 3. Servicios

Clases para lógica de negocio reutilizable y llamadas HTTP.

```typescript
@Injectable({ providedIn: 'root' })
export class ProductoService {
  constructor(private http: HttpClient) { }

  obtenerTodos(): Observable<Producto[]> {
    return this.http.get<Producto[]>('/api/productos');
  }
}
```

### 4. Inyección de Dependencias

Angular inyecta automáticamente las dependencias.

```typescript
export class ProductoComponent {
  // Angular crea e inyecta el servicio
  constructor(private productoService: ProductoService) { }
}
```

## Data Binding

Angular ofrece 4 tipos de binding:

### Interpolación
```html
<p>{{ producto.nombre }}</p>
```

### Property Binding
```html
<img [src]="producto.imagen">
<button [disabled]="!formularioValido">
```

### Event Binding
```html
<button (click)="guardar()">Guardar</button>
<input (keyup.enter)="buscar()">
```

### Two-Way Binding
```html
<input [(ngModel)]="producto.nombre">
```

## Directivas

### *ngIf - Condicional
```html
<div *ngIf="productos.length > 0">
  Hay {{ productos.length }} productos
</div>
<div *ngIf="cargando; else contenido">
  Cargando...
</div>
<ng-template #contenido>
  <!-- contenido -->
</ng-template>
```

### *ngFor - Iteración
```html
<ul>
  <li *ngFor="let producto of productos; let i = index">
    {{ i + 1 }}. {{ producto.nombre }}
  </li>
</ul>
```

### [ngClass] - Clases dinámicas
```html
<div [ngClass]="{
  'activo': producto.activo,
  'destacado': producto.precio > 100
}">
```

## Pipes

Transforman datos en el template:

```html
<!-- Mayúsculas -->
{{ nombre | uppercase }}

<!-- Formato moneda -->
{{ precio | currency:'USD' }}

<!-- Formato fecha -->
{{ fecha | date:'dd/MM/yyyy' }}

<!-- JSON (debug) -->
{{ objeto | json }}
```

## HTTP Client

### Configuración
```typescript
// app.config.ts
import { provideHttpClient } from '@angular/common/http';

export const appConfig = {
  providers: [provideHttpClient()]
};
```

### Uso en Servicio
```typescript
@Injectable({ providedIn: 'root' })
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // GET
  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${this.apiUrl}/${endpoint}`);
  }

  // POST
  post<T>(endpoint: string, data: any): Observable<T> {
    return this.http.post<T>(`${this.apiUrl}/${endpoint}`, data);
  }

  // PUT
  put<T>(endpoint: string, data: any): Observable<T> {
    return this.http.put<T>(`${this.apiUrl}/${endpoint}`, data);
  }

  // DELETE
  delete(endpoint: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${endpoint}`);
  }
}
```

## Observables y RxJS

Los Observables son streams de datos asíncronos.

### Suscripción
```typescript
this.productoService.obtenerTodos().subscribe({
  next: (data) => this.productos = data,
  error: (err) => console.error(err),
  complete: () => console.log('Completado')
});
```

### Operadores comunes
```typescript
import { map, filter, catchError } from 'rxjs/operators';

this.productoService.obtenerTodos().pipe(
  map(productos => productos.filter(p => p.activo)),
  catchError(error => {
    console.error(error);
    return of([]);
  })
).subscribe(data => this.productos = data);
```

## Routing

### Configuración
```typescript
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'productos', component: ProductoListComponent },
  { path: 'productos/:id', component: ProductoDetailComponent },
  { path: '**', redirectTo: '' }
];
```

### Navegación
```html
<a routerLink="/productos">Productos</a>
<a [routerLink]="['/productos', producto.id]">Ver</a>
```

```typescript
constructor(private router: Router) { }

irAProducto(id: number) {
  this.router.navigate(['/productos', id]);
}
```

## Formularios

### Template-Driven (ngModel)
```html
<form #form="ngForm" (ngSubmit)="guardar(form)">
  <input name="nombre" [(ngModel)]="producto.nombre" required>
  <button [disabled]="!form.valid">Guardar</button>
</form>
```

### Reactive Forms
```typescript
export class ProductoFormComponent {
  form = new FormGroup({
    nombre: new FormControl('', [Validators.required]),
    precio: new FormControl(0, [Validators.min(0)])
  });

  guardar() {
    if (this.form.valid) {
      console.log(this.form.value);
    }
  }
}
```

## Estructura de Proyecto Recomendada

```
src/
├── app/
│   ├── components/       # Componentes de UI
│   ├── services/         # Servicios
│   ├── models/           # Interfaces/Modelos
│   ├── guards/           # Guards de rutas
│   ├── interceptors/     # HTTP Interceptors
│   ├── pipes/            # Pipes personalizados
│   └── shared/           # Código compartido
├── assets/               # Archivos estáticos
└── environments/         # Configuración por ambiente
```

## Comandos Angular CLI

```bash
# Crear proyecto
ng new mi-proyecto

# Generar componente
ng generate component nombre

# Generar servicio
ng generate service nombre

# Ejecutar desarrollo
ng serve

# Construir producción
ng build --prod
```

## Lifecycle Hooks

```typescript
export class MiComponente implements OnInit, OnDestroy {
  ngOnInit() {
    // Al inicializar (después del constructor)
  }

  ngOnDestroy() {
    // Al destruir (cleanup, unsubscribe)
  }
}
```

| Hook | Descripción |
|------|-------------|
| ngOnInit | Inicialización |
| ngOnDestroy | Limpieza |
| ngOnChanges | Cuando @Input cambia |
| ngAfterViewInit | Vista renderizada |
