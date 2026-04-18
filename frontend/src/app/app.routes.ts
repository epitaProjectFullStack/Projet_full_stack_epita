import {Routes} from '@angular/router';

import {Admin} from './page/admin/admin';
import {ArticleNew} from './page/article-new/article-new';
import {ArticleReview} from './page/article-review/article-review';
import {ArticleShow} from './page/article-show/article-show';
import {MainPage} from './page/main-page/main-page';
import {Register} from './page/register/register';
import {ReviewerBoard} from './page/reviewer-board/reviewer-board';
import {Signin} from './page/signin/signin';

export const routesDict = [
  {name: 'Home', route: {path: '', component: MainPage}},
  {name: 'SignIn', route: {path: 'signin', component: Signin}},
  {name: 'Register', route: {path: 'register', component: Register}},
  {name: 'New', route: {path: 'new', component: ArticleNew}},
  {name: 'Admin', route: {path: 'admin', component: Admin}},
  {name: 'Review', route: {path: 'reviewer', component: ReviewerBoard}}
];
export const routes: Routes = [
  {path: 'article/:id', component: ArticleShow},
  {path: 'article/review/:id', component: ArticleReview},
  ...routesDict.map((elem) => elem.route)
];
