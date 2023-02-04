import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPagador } from '../pagador.model';

@Component({
  selector: 'jhi-pagador-detail',
  templateUrl: './pagador-detail.component.html',
})
export class PagadorDetailComponent implements OnInit {
  pagador: IPagador | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pagador }) => {
      this.pagador = pagador;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
