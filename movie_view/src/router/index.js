import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import UserHomeView from '@/views/UserHomeView.vue'
import LoginView from '@/views/LoginView.vue'
import MovieList from '@/views/movie/MovieList.vue'
import MovieDetail from '@/views/movie/MovieDetail.vue'
import CinemaList from '@/views/cinema/CinemaList.vue';
import CinemaDetail from '@/views/cinema/CinemaDetail.vue';
import HallList from '@/views/cinema/HallList.vue';
import OrderList from '@/views/order/OrderList.vue';
import ScheduleList from '@/views/schedule/ScheduleList.vue';
import RegisterView from '@/views/RegisterView.vue'
import UserMovieList from '@/views/movie/UserMovieList.vue';
import UserCinemaList from '@/views/cinema/UserCinemaList.vue';
import UserOrderList from '@/views/order/UserOrderList.vue';
import UserScheduleList from '@/views/schedule/UserScheduleList.vue';
import UserList from '@/views/user/UserList.vue';
import Profile from '@/views/user/Profile.vue';
import SeatList from '@/views/schedule/SeatList.vue';
import UserSeatList from '@/views/schedule/UserSeatList.vue';

const routes = [
  {
    path: '/home',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/userhome',
    name: 'UserHome',
    component: UserHomeView
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/movies',
    name: 'MovieList',
    component: MovieList
  },
  {
    path: '/movie/:id',
    name: 'MovieDetail',
    component: MovieDetail,
    props: true // 允许将 :id 当作 prop 传给组件
  },
   {
    path: '/cinemas',
    name: 'CinemaList',
    component: CinemaList
  },
  {
    path: '/cinema/:id',
    name: 'CinemaDetail',
    component: CinemaDetail
  },
  {
    path: '/Hall/:id',
    name: 'HallList',
    component: HallList
  },
  {
    path: '/orders',
    name: 'OrderList',
    component: OrderList
  },
  {
    path: '/schedules',
    name: 'ScheduleList',
    component: ScheduleList
  },
  {
  path: '/register',
  name: 'Register',
  component: () => import('@/views/RegisterView.vue')
  },
  {
  path: '/user-movies',  // 用户端电影列表
  name: 'UserMovieList',
  component: UserMovieList
  },
  {
    path: '/user-cinemas',
    name: 'UserCinemaList',
    component: UserCinemaList
  },
  {
    path: '/user-orders',
    name: 'UserOrderList',
    component: UserOrderList
  },
  {
    path: '/user-schedules',
    name: 'UserScheduleList',
    component: UserScheduleList
  },
  {
    path: '/users',
    name: 'UserList',
    component: UserList
  },
  {
    path: '/profile/:id',
    name: 'profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
  path: '/seat-list/:hallId',
  name: 'SeatList',
  component: SeatList,
  props: true
},
{
  path: '/user-seat-list/:hallId/:id',
  name: 'UserSeatList',
  component: UserSeatList,
  props: true
},
{
  path: '/create-order',
  name: 'CreateOrder',
  component: () => import('@/views/Order/CreateOrder.vue')
}
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      next('/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router

