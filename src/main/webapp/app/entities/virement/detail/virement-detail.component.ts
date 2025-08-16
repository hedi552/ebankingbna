import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVirement } from '../virement.model';

@Component({
  selector: 'jhi-virement-detail',
  templateUrl: './virement-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VirementDetailComponent {
  virement = input<IVirement | null>(null);

  previousState(): void {
    window.history.back();
  }
}
