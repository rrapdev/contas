import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PagadorFormService, PagadorFormGroup } from './pagador-form.service';
import { IPagador } from '../pagador.model';
import { PagadorService } from '../service/pagador.service';

@Component({
  selector: 'jhi-pagador-update',
  templateUrl: './pagador-update.component.html',
})
export class PagadorUpdateComponent implements OnInit {
  isSaving = false;
  pagador: IPagador | null = null;

  editForm: PagadorFormGroup = this.pagadorFormService.createPagadorFormGroup();

  constructor(
    protected pagadorService: PagadorService,
    protected pagadorFormService: PagadorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pagador }) => {
      this.pagador = pagador;
      if (pagador) {
        this.updateForm(pagador);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pagador = this.pagadorFormService.getPagador(this.editForm);
    if (pagador.id !== null) {
      this.subscribeToSaveResponse(this.pagadorService.update(pagador));
    } else {
      this.subscribeToSaveResponse(this.pagadorService.create(pagador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPagador>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pagador: IPagador): void {
    this.pagador = pagador;
    this.pagadorFormService.resetForm(this.editForm, pagador);
  }
}
