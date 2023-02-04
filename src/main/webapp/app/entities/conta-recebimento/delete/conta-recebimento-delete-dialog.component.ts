import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContaRecebimento } from '../conta-recebimento.model';
import { ContaRecebimentoService } from '../service/conta-recebimento.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './conta-recebimento-delete-dialog.component.html',
})
export class ContaRecebimentoDeleteDialogComponent {
  contaRecebimento?: IContaRecebimento;

  constructor(protected contaRecebimentoService: ContaRecebimentoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contaRecebimentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
