import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContaRecebimentoFormService, ContaRecebimentoFormGroup } from './conta-recebimento-form.service';
import { IContaRecebimento } from '../conta-recebimento.model';
import { ContaRecebimentoService } from '../service/conta-recebimento.service';
import { IPagador } from 'app/entities/pagador/pagador.model';
import { PagadorService } from 'app/entities/pagador/service/pagador.service';
import { ICategoriaRecebimento } from 'app/entities/categoria-recebimento/categoria-recebimento.model';
import { CategoriaRecebimentoService } from 'app/entities/categoria-recebimento/service/categoria-recebimento.service';
import { ICaixa } from 'app/entities/caixa/caixa.model';
import { CaixaService } from 'app/entities/caixa/service/caixa.service';
import { Periodicidade } from 'app/entities/enumerations/periodicidade.model';

@Component({
  selector: 'jhi-conta-recebimento-update',
  templateUrl: './conta-recebimento-update.component.html',
})
export class ContaRecebimentoUpdateComponent implements OnInit {
  isSaving = false;
  contaRecebimento: IContaRecebimento | null = null;
  periodicidadeValues = Object.keys(Periodicidade);

  pagadorsSharedCollection: IPagador[] = [];
  categoriaRecebimentosSharedCollection: ICategoriaRecebimento[] = [];
  caixasSharedCollection: ICaixa[] = [];

  editForm: ContaRecebimentoFormGroup = this.contaRecebimentoFormService.createContaRecebimentoFormGroup();

  constructor(
    protected contaRecebimentoService: ContaRecebimentoService,
    protected contaRecebimentoFormService: ContaRecebimentoFormService,
    protected pagadorService: PagadorService,
    protected categoriaRecebimentoService: CategoriaRecebimentoService,
    protected caixaService: CaixaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePagador = (o1: IPagador | null, o2: IPagador | null): boolean => this.pagadorService.comparePagador(o1, o2);

  compareCategoriaRecebimento = (o1: ICategoriaRecebimento | null, o2: ICategoriaRecebimento | null): boolean =>
    this.categoriaRecebimentoService.compareCategoriaRecebimento(o1, o2);

  compareCaixa = (o1: ICaixa | null, o2: ICaixa | null): boolean => this.caixaService.compareCaixa(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contaRecebimento }) => {
      this.contaRecebimento = contaRecebimento;
      if (contaRecebimento) {
        this.updateForm(contaRecebimento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contaRecebimento = this.contaRecebimentoFormService.getContaRecebimento(this.editForm);
    if (contaRecebimento.id !== null) {
      this.subscribeToSaveResponse(this.contaRecebimentoService.update(contaRecebimento));
    } else {
      this.subscribeToSaveResponse(this.contaRecebimentoService.create(contaRecebimento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContaRecebimento>>): void {
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

  protected updateForm(contaRecebimento: IContaRecebimento): void {
    this.contaRecebimento = contaRecebimento;
    this.contaRecebimentoFormService.resetForm(this.editForm, contaRecebimento);

    this.pagadorsSharedCollection = this.pagadorService.addPagadorToCollectionIfMissing<IPagador>(
      this.pagadorsSharedCollection,
      contaRecebimento.pagador
    );
    this.categoriaRecebimentosSharedCollection =
      this.categoriaRecebimentoService.addCategoriaRecebimentoToCollectionIfMissing<ICategoriaRecebimento>(
        this.categoriaRecebimentosSharedCollection,
        contaRecebimento.categoriaRecebimento
      );
    this.caixasSharedCollection = this.caixaService.addCaixaToCollectionIfMissing<ICaixa>(
      this.caixasSharedCollection,
      contaRecebimento.caixa
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pagadorService
      .query()
      .pipe(map((res: HttpResponse<IPagador[]>) => res.body ?? []))
      .pipe(
        map((pagadors: IPagador[]) =>
          this.pagadorService.addPagadorToCollectionIfMissing<IPagador>(pagadors, this.contaRecebimento?.pagador)
        )
      )
      .subscribe((pagadors: IPagador[]) => (this.pagadorsSharedCollection = pagadors));

    this.categoriaRecebimentoService
      .query()
      .pipe(map((res: HttpResponse<ICategoriaRecebimento[]>) => res.body ?? []))
      .pipe(
        map((categoriaRecebimentos: ICategoriaRecebimento[]) =>
          this.categoriaRecebimentoService.addCategoriaRecebimentoToCollectionIfMissing<ICategoriaRecebimento>(
            categoriaRecebimentos,
            this.contaRecebimento?.categoriaRecebimento
          )
        )
      )
      .subscribe((categoriaRecebimentos: ICategoriaRecebimento[]) => (this.categoriaRecebimentosSharedCollection = categoriaRecebimentos));

    this.caixaService
      .query()
      .pipe(map((res: HttpResponse<ICaixa[]>) => res.body ?? []))
      .pipe(map((caixas: ICaixa[]) => this.caixaService.addCaixaToCollectionIfMissing<ICaixa>(caixas, this.contaRecebimento?.caixa)))
      .subscribe((caixas: ICaixa[]) => (this.caixasSharedCollection = caixas));
  }
}
