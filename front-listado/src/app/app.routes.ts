import { Routes } from '@angular/router';
import { ListadoEventosComponent } from './components/listado-eventos/listado-eventos.component';
import { FavoritosComponent } from './components/favoritos/favoritos.component';
import { ComprasComponent } from './components/compras/compras.component';
import { EventosFormComponent } from './components/eventos-form/eventos-form.component';

export const routes: Routes = [
  { path: '', component: ListadoEventosComponent },
  { path: 'eventos', component: ListadoEventosComponent },
  { path: 'eventos/nuevo', component: EventosFormComponent },
  { path: 'eventos/:id/editar', component: EventosFormComponent },
  { path: 'favoritos', component: FavoritosComponent },
  { path: 'compras', component: ComprasComponent },
  { path: '**', redirectTo: '' }
];
