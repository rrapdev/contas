import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoriaPagamento } from '../categoria-pagamento.model';
import { CategoriaPagamentoService } from '../service/categoria-pagamento.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './categoria-pagamento-delete-dialog.component.html',
})
export class CategoriaPagamentoDeleteDialogComponent {
  categoriaPagamento?: ICategoriaPagamento;

  constructor(protected categoriaPagamentoService: CategoriaPagamentoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoriaPagamentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
