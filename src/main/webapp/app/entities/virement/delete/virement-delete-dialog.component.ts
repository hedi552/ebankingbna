import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVirement } from '../virement.model';
import { VirementService } from '../service/virement.service';

@Component({
  templateUrl: './virement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VirementDeleteDialogComponent {
  virement?: IVirement;

  protected virementService = inject(VirementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.virementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
