import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/auth/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { SearchComponent } from './components/public/search/search.component';
import { DetailComponent } from './components/public/detail/detail.component';
import { BranchDetailComponent } from './components/public/branch-detail/branch-detail.component';

import { ProfileComponent } from './components/profile/profile.component';
import { VerifyAccountComponent } from './components/auth/verify-account/verify-account.component';
import { DashboardComponent } from './components/admin/dashboard/dashboard.component';
import { MyApplicationComponent } from './components/my-application/my-application.component';
import { FavouriteComponent } from './components/favourite/favourite.component';
import { ChangepasswordComponent } from './components/changepassword/changepassword.component';
import { ConfirmforgotpasswordComponent } from './components/confirmforgotpassword/confirmforgotpassword.component';
import { ChangeforgotpasswordComponent } from './components/changeforgotpassword/changeforgotpassword.component';
import { EnteremailforgotComponent } from './components/enteremailforgot/enteremailforgot.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { AboutComponent } from './components/about/about.component';
import { ContactComponent } from './components/contact/contact.component';
import { PolicyComponent } from './components/policy/policy.component';
import { ChatComponent } from './components/chat/chat.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'search', component: SearchComponent },
  { path: 'detail/:jobId', component: DetailComponent },
  { path: 'branch/:branchId', component: BranchDetailComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'verifyAccount', component: VerifyAccountComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'myApplication', component: MyApplicationComponent },
  { path: 'favourite', component: FavouriteComponent },
  { path: 'changepassword', component: ChangepasswordComponent },
  { path: 'verifyforgotpassword', component: ConfirmforgotpasswordComponent },
  { path: 'changeforgotpassword', component: ChangeforgotpasswordComponent },
  { path: 'forgotemail', component: EnteremailforgotComponent },
  { path: 'notfound', component: NotFoundComponent },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'policy', component: PolicyComponent },
  { path: 'chat', component: ChatComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
