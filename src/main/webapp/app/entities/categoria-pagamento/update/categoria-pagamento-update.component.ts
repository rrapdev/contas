import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CategoriaPagamentoFormService, CategoriaPagamentoFormGroup } from './categoria-pagamento-form.service';
import { ICategoriaPagamento } from '../categoria-pagamento.model';
import { CategoriaPagamentoService } from '../service/categoria-pagamento.service';

@Component({
  selector: 'jhi-categoria-pagamento-update',
  templateUrl: './categoria-pagamento-update.component.html',
})
export class CategoriaPagamentoUpdateComponent implements OnInit {
  isSaving = false;
  categoriaPagamento: ICategoriaPagamento | null = null;

  editForm: CategoriaPagamentoFormGroup = this.categoriaPagamentoFormService.createCategoriaPagamentoFormGroup();

  constructor(
    protected categoriaPagamentoService: CategoriaPagamentoService,
    protected categoriaPagamentoFormService: CategoriaPagamentoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaPagamento }) => {
      this.categoriaPagamento = categoriaPagamento;
      if (categoriaPagamento) {
        this.updateForm(categoriaPagamento);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoriaPagamento = this.categoriaPagamentoFormService.getCategoriaPagamento(this.editForm);
    if (categoriaPagamento.id !== null) {
      this.subscribeToSaveResponse(this.categoriaPagamentoService.update(categoriaPagamento));
    } else {
      this.subscribeToSaveResponse(this.categoriaPagamentoService.create(categoriaPagamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaPagamento>>): void {
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

  protected updateForm(categoriaPagamento: ICategoriaPagamento): void {
    this.categoriaPagamento = categoriaPagamento;
    this.categoriaPagamentoFormService.resetForm(this.editForm, categoriaPagamento);
  }
}
