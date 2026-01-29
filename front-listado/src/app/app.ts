import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ListadoEventosComponent } from './components/listado-eventos/listado-eventos.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ListadoEventosComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('front-listado');
}
