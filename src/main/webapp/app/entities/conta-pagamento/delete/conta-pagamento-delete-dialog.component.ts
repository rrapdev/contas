import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContaPagamento } from '../conta-pagamento.model';
import { ContaPagamentoService } from '../service/conta-pagamento.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './conta-pagamento-delete-dialog.component.html',
})
export class ContaPagamentoDeleteDialogComponent {
  contaPagamento?: IContaPagamento;

  constructor(protected contaPagamentoService: ContaPagamentoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contaPagamentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
