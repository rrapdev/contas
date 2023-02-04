import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BeneficiarioFormService, BeneficiarioFormGroup } from './beneficiario-form.service';
import { IBeneficiario } from '../beneficiario.model';
import { BeneficiarioService } from '../service/beneficiario.service';

@Component({
  selector: 'jhi-beneficiario-update',
  templateUrl: './beneficiario-update.component.html',
})
export class BeneficiarioUpdateComponent implements OnInit {
  isSaving = false;
  beneficiario: IBeneficiario | null = null;

  editForm: BeneficiarioFormGroup = this.beneficiarioFormService.createBeneficiarioFormGroup();

  constructor(
    protected beneficiarioService: BeneficiarioService,
    protected beneficiarioFormService: BeneficiarioFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beneficiario }) => {
      this.beneficiario = beneficiario;
      if (beneficiario) {
        this.updateForm(beneficiario);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const beneficiario = this.beneficiarioFormService.getBeneficiario(this.editForm);
    if (beneficiario.id !== null) {
      this.subscribeToSaveResponse(this.beneficiarioService.update(beneficiario));
    } else {
      this.subscribeToSaveResponse(this.beneficiarioService.create(beneficiario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBeneficiario>>): void {
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

  protected updateForm(beneficiario: IBeneficiario): void {
    this.beneficiario = beneficiario;
    this.beneficiarioFormService.resetForm(this.editForm, beneficiario);
  }
}
