import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { ProfesorInfoUpdateComponent } from 'app/entities/profesor-info/profesor-info-update.component';
import { ProfesorInfoService } from 'app/entities/profesor-info/profesor-info.service';
import { ProfesorInfo } from 'app/shared/model/profesor-info.model';

describe('Component Tests', () => {
  describe('ProfesorInfo Management Update Component', () => {
    let comp: ProfesorInfoUpdateComponent;
    let fixture: ComponentFixture<ProfesorInfoUpdateComponent>;
    let service: ProfesorInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [ProfesorInfoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProfesorInfoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProfesorInfoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProfesorInfoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProfesorInfo(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProfesorInfo();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
