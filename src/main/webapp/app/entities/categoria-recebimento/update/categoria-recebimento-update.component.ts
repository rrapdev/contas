import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CategoriaRecebimentoFormService, CategoriaRecebimentoFormGroup } from './categoria-recebimento-form.service';
import { ICategoriaRecebimento } from '../categoria-recebimento.model';
import { CategoriaRecebimentoService } from '../service/categoria-recebimento.service';

@Component({
  selector: 'jhi-categoria-recebimento-update',
  templateUrl: './categoria-recebimento-update.component.html',
})
export class CategoriaRecebimentoUpdateComponent implements OnInit {
  isSaving = false;
  categoriaRecebimento: ICategoriaRecebimento | null = null;

  editForm: CategoriaRecebimentoFormGroup = this.categoriaRecebimentoFormService.createCategoriaRecebimentoFormGroup();

  constructor(
    protected categoriaRecebimentoService: CategoriaRecebimentoService,
    protected categoriaRecebimentoFormService: CategoriaRecebimentoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaRecebimento }) => {
      this.categoriaRecebimento = categoriaRecebimento;
      if (categoriaRecebimento) {
        this.updateForm(categoriaRecebimento);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoriaRecebimento = this.categoriaRecebimentoFormService.getCategoriaRecebimento(this.editForm);
    if (categoriaRecebimento.id !== null) {
      this.subscribeToSaveResponse(this.categoriaRecebimentoService.update(categoriaRecebimento));
    } else {
      this.subscribeToSaveResponse(this.categoriaRecebimentoService.create(categoriaRecebimento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaRecebimento>>): void {
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

  protected updateForm(categoriaRecebimento: ICategoriaRecebimento): void {
    this.categoriaRecebimento = categoriaRecebimento;
    this.categoriaRecebimentoFormService.resetForm(this.editForm, categoriaRecebimento);
  }
}
