import pandas as pd
import numpy as np
import json
import joblib

model = joblib.load("chatbot_model.joblib")

def cosine_similarity(vector1, vector2):
    dot_product = np.dot(vector1, vector2)
    norm1 = np.linalg.norm(vector1)
    norm2 = np.linalg.norm(vector2)
    cosine_sim = dot_product / (norm1 * norm2)
    return cosine_sim

def get_dataset():
    df = pd.read_csv("model_dataset.csv")
    df['embedding'] = df['embedding'].apply(json.loads)

    return df

def send_answer(text):
    # 사용자 발화를 인코딩해서 임베딩 결과를 뽑아줌
    embedding = model.encode(text)
    df = get_dataset()

    # 사용자 발화와 유저 발화 데이터셋을 비교하여 유사도를 distance 에 저장
    df['distance'] = df['embedding'].map(lambda x: cosine_similarity(embedding, x).squeeze())

    # distance (유사도) 가 가장 높은 대화셋을 추출
    answer = df.loc[df["distance"].idxmax()]

    final_answer = ""
    # 유사도에 따른 답변 제공
    if (answer["distance"] < 0.6):
        final_answer = "아직 그 질문에 대한 답변이 준비되지 않았어요."
    else:
        final_answer = answer["챗봇"]

    return final_answer

