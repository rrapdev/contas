import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContaRecebimento } from '../conta-recebimento.model';

@Component({
  selector: 'jhi-conta-recebimento-detail',
  templateUrl: './conta-recebimento-detail.component.html',
})
export class ContaRecebimentoDetailComponent implements OnInit {
  contaRecebimento: IContaRecebimento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contaRecebimento }) => {
      this.contaRecebimento = contaRecebimento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
