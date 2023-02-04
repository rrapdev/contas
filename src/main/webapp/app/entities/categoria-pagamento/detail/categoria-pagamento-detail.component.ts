import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategoriaPagamento } from '../categoria-pagamento.model';

@Component({
  selector: 'jhi-categoria-pagamento-detail',
  templateUrl: './categoria-pagamento-detail.component.html',
})
export class CategoriaPagamentoDetailComponent implements OnInit {
  categoriaPagamento: ICategoriaPagamento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaPagamento }) => {
      this.categoriaPagamento = categoriaPagamento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
