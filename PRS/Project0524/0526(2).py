import numpy as np
from surprise import SVD, Reader, Dataset
from scipy.sparse.linalg import svds
import pandas as pd
from surprise.model_selection import GridSearchCV


##

og_recipe= pd.read_json('C:/RecoSys/merged_recipe.json', encoding='UTF-8')

#2열짜리로 1자로 쭉 생성
cols=['id','name']
recipe_name = pd.DataFrame(columns=cols)

idx=0
for rid, rname in zip(og_recipe['id'], og_recipe['name']):
    list=[rid,rname]

    recipe_name.loc[idx]=list
    print(idx)
    idx=idx+1
##



# get user,recipe and rating file and convert as matrix, treat with pandas
u_cols=['name', 'id', 'pwd']
users= pd.read_csv('C:/RecoSys/users.csv',names=u_cols, header=None, encoding='UTF-8')
recipes=pd.read_csv('C:/RecoSys/new_recipe_tags.csv',header=0, encoding='utf-8')
r_cols=['recipe_id', 'user_id', 'rating']
ratings= pd.read_csv('C:/RecoSys/new_rating.csv',names=r_cols, header=None, encoding='UTF-8')
ratings['rating']=ratings['rating'].replace([1,2,3,4,5],5)

user_list=[0]*31000
idx=0
for i in ratings['user_id']:
         if i not in user_list:
             user_list[idx]=i
             idx=idx+1
#user_id='jtion2'
#user_id='77726315'
user_id='jf1000'
user_pwd='123456'

# get params by GridSearchCV
traindata=Dataset.load_from_df(ratings, Reader())
#param_grid = {'n_epochs': [20,40,60], 'n_factors': [10,20,40,80]}
param_grid = {'n_epochs': [10], 'n_factors': [70]}
gs = GridSearchCV(SVD, param_grid, measures=['rmse', 'mse'], cv=3)
gs.fit(traindata)
print()
pr=gs.best_params['rmse']
print('best RMSE: ',gs.best_score['rmse'],'at params: n_epochs : ', pr['n_epochs'],' / ','n_factors : ',pr['n_factors'])

df_ratings=ratings.pivot_table(
    values='rating',
    index='user_id',
    columns='recipe_id'
).fillna(0)

mat=df_ratings.to_numpy()
user_mean=np.mean(mat, axis=1)

#compose model with ratinig data and params
U, sigma, Vt = svds(df_ratings, k=pr['n_factors'],maxiter=pr['n_epochs'])  # k= 1~ 957
sigma=np.diag(sigma)
svd_user_pred_ratings= np.dot(np.dot(U,sigma),Vt)+user_mean.reshape(-1,1)
df_svd= pd.DataFrame(svd_user_pred_ratings,columns=df_ratings.columns)
print('-- svd --')
print(df_svd)

def recommend_recipes(df_svd,user_id,recipe_df,ratings_df,rec_num=10):
    user_row_num= user_list.index(user_id)
    sorted_user_pred=df_svd.loc[user_row_num].sort_values(ascending=False)
    user_data=ratings_df[ratings_df.user_id==user_id]
    user_history=user_data.merge(recipe_df, on='recipe_id').sort_values(['rating'],ascending=False)
    recmd=recipe_df[~recipe_df['recipe_id'].isin(user_history['recipe_id'])]
    recmd=recmd.merge(pd.DataFrame(sorted_user_pred).reset_index(), on='recipe_id')
    recmd=recmd.rename(columns={user_row_num: 'Predictions'}).sort_values('Predictions',ascending=False).iloc[:rec_num, :]

    return user_history, recmd

#for dealing with model, take recipe list by throwing user's id

user_name=input("input id: ")
print('-- info -- ')
print('name:{},  id:{}, pwd:{}'.format(user_name,user_id,user_pwd))
already_rated,predictions= recommend_recipes(df_svd, user_id, recipes, ratings, 10)
print(already_rated[['recipe_id','rating']])
print(predictions[['recipe_id','Predictions']])