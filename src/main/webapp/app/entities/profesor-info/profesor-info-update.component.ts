import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProfesorInfo, ProfesorInfo } from 'app/shared/model/profesor-info.model';
import { ProfesorInfoService } from './profesor-info.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-profesor-info-update',
  templateUrl: './profesor-info-update.component.html'
})
export class ProfesorInfoUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    user: [null, Validators.required]
  });

  constructor(
    protected profesorInfoService: ProfesorInfoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profesorInfo }) => {
      this.updateForm(profesorInfo);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(profesorInfo: IProfesorInfo): void {
    this.editForm.patchValue({
      id: profesorInfo.id,
      user: profesorInfo.user
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profesorInfo = this.createFromForm();
    if (profesorInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.profesorInfoService.update(profesorInfo));
    } else {
      this.subscribeToSaveResponse(this.profesorInfoService.create(profesorInfo));
    }
  }

  private createFromForm(): IProfesorInfo {
    return {
      ...new ProfesorInfo(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfesorInfo>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
