import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMondat } from '../mondat.model';
import { MondatService } from '../service/mondat.service';

@Component({
  templateUrl: './mondat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MondatDeleteDialogComponent {
  mondat?: IMondat;

  protected mondatService = inject(MondatService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mondatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
