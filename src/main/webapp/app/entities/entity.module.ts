import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student-info',
        loadChildren: () => import('./student-info/student-info.module').then(m => m.LicentaStudentInfoModule)
      },
      {
        path: 'profesor-info',
        loadChildren: () => import('./profesor-info/profesor-info.module').then(m => m.LicentaProfesorInfoModule)
      },
      {
        path: 'licenta',
        loadChildren: () => import('./licenta/licenta.module').then(m => m.LicentaLicentaModule)
      },
      {
        path: 'aplicare-licenta',
        loadChildren: () => import('./aplicare-licenta/aplicare-licenta.module').then(m => m.LicentaAplicareLicentaModule)
      },
      {
        path: 'aplicare-consultatie',
        loadChildren: () => import('./aplicare-consultatie/aplicare-consultatie.module').then(m => m.LicentaAplicareConsultatieModule)
      },
      {
        path: 'consultatie',
        loadChildren: () => import('./consultatie/consultatie.module').then(m => m.LicentaConsultatieModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class LicentaEntityModule {}
