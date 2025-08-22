import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMondat } from '../mondat.model';

@Component({
  selector: 'jhi-mondat-detail',
  templateUrl: './mondat-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MondatDetailComponent {
  mondat = input<IMondat | null>(null);

  previousState(): void {
    window.history.back();
  }
}
