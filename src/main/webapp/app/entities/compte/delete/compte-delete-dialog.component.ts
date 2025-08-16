import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICompte } from '../compte.model';
import { CompteService } from '../service/compte.service';

@Component({
  templateUrl: './compte-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CompteDeleteDialogComponent {
  compte?: ICompte;

  protected compteService = inject(CompteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
