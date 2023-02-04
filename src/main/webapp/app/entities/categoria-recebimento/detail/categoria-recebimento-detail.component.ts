import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategoriaRecebimento } from '../categoria-recebimento.model';

@Component({
  selector: 'jhi-categoria-recebimento-detail',
  templateUrl: './categoria-recebimento-detail.component.html',
})
export class CategoriaRecebimentoDetailComponent implements OnInit {
  categoriaRecebimento: ICategoriaRecebimento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaRecebimento }) => {
      this.categoriaRecebimento = categoriaRecebimento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
