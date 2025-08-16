import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICompte } from '../compte.model';

@Component({
  selector: 'jhi-compte-detail',
  templateUrl: './compte-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CompteDetailComponent {
  compte = input<ICompte | null>(null);

  previousState(): void {
    window.history.back();
  }
}
