import { Routes } from '@angular/router';
import { ListadoEventosComponent } from './components/listado-eventos/listado-eventos.component';
import { FavoritosComponent } from './components/favoritos/favoritos.component';
import { ComprasComponent } from './components/compras/compras.component';

export const routes: Routes = [
  { path: '', component: ListadoEventosComponent },
  { path: 'favoritos', component: FavoritosComponent },
  { path: 'compras', component: ComprasComponent },
  { path: '**', redirectTo: '' }
];
