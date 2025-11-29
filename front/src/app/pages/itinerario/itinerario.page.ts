import { Component } from '@angular/core';
import { NavBar } from "../../components/nav-bar/nav-bar.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BoxItinerarioComponent } from "../../components/box-itinerario/box-itinerario.component";
import { ItinerarioResponse } from '../../model/Itinerario';

@Component({
  selector: 'app-itinerario',
  imports: [CommonModule, RouterLink, NavBar, FooterComponent, BoxItinerarioComponent],
  templateUrl: './itinerario.page.html',
  styleUrl: './itinerario.page.scss'
})
export class ItinerarioPage {
  listaItinerarioData: ItinerarioResponse[] = []
}
