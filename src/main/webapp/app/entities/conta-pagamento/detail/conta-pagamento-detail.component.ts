import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContaPagamento } from '../conta-pagamento.model';

@Component({
  selector: 'jhi-conta-pagamento-detail',
  templateUrl: './conta-pagamento-detail.component.html',
})
export class ContaPagamentoDetailComponent implements OnInit {
  contaPagamento: IContaPagamento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contaPagamento }) => {
      this.contaPagamento = contaPagamento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
