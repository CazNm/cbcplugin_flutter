/*!
 * \file seed.h
 * \brief SEED 암호 알고리즘 (관련표준 : TTAS.KO-12.0004 : 128비트 블록암호알고리즘(SEED))
 * \author
 * Copyright (c) 2010 by \<KISA\>
 */
#ifndef KISA_SEED_H
#define KISA_SEED_H

#include <jni.h>

#ifdef  __cplusplus
extern "C" {
#endif

#define SEED_BLOCK_SIZE 16			/*!< SEED 블럭 크기*/
#define SEED_ENCRYPT	1			/*!< SEED 암호화 모드*/
#define SEED_DECRYPT	0			/*!< SEED 복호화 모드*/

	/*!
	 * \brief
	 * SEED 내부 엔진 암호화를 위한 SEED Key 구조체
	 * \remarks
	 * unsigned int key_data[32] 자료형
	 */
	typedef struct kisa_seed_key_st {
		unsigned int key_data[32];
	} KISA_SEED_KEY;

	/*!
	* \brief
	* SEED 초기화를 위한 암호화키 지정 함수
	* \param user_key
	* 사용자가 지정하는 입력 키 (16 bytes)
	* \param ks
	* 사용자가 지정하는 키가 저장되는 키 구조체
	* \remarks
	* const unsigned char *user_key의 크기는 반드시 16 bytes 가 입력되어야 하고 키구조체(KISA_SEED_KEY *ks)는 메모리 할당이 되어있어야 함
	*/
    void KISA_SEED_init(const unsigned char *userKey, KISA_SEED_KEY *ks);

	/*!
	* \brief
	* SEED 알고리즘 단일 블럭 암호화 함수
	* \param in
	* 사용자 입력 평문(16 bytes)
	* \param out
	* 사용자 입력에 대한 출력 암호문(16 bytes)
	* \param seed_key
	* KISA_SEED_init로 사용자 키가 설정된 키 배열
	* \remarks
	* -# 사용자 입력 평문(const unsigned char *in)의 크기는 반드시 16 bytes 를 입력
	* -# 출력 암호문(unsigned char *out)는 16 bytes 이상 메모리 할당이 되어 있어야 하며, 16 bytes 암호문에 저장됨
	*/
	void KISA_SEED_encrypt_block(const unsigned char *in, unsigned char *out, const int *seed_key);
	void KISA_SEED_encrypt_block_(const unsigned char *in, unsigned char *out, const KISA_SEED_KEY *ks);
	
	/*!
	* \brief
	* SEED 알고리즘 단일 블럭 복호화 함수
	* \param in
	* 사용자 입력 암호문(16 bytes)
	* \param out
	* 사용자 입력에 대한 출력 평문(16 bytes)
	* \param seed_key
	* KISA_SEED_init로 사용자 키가 설정된 키 배열
	* \remarks
	* -# 사용자 입력 암호문(const unsigned char *in)의 크기는 반드시 16 bytes 를 입력
	* -# 출력 평문(unsigned char *out)는 16 bytes 이상 메모리 할당이 되어 있어야 하며, 16 bytes 평문에 저장됨
	*/
	void KISA_SEED_decrypt_block(const unsigned char *in, unsigned char *out, const int *seed_key);

JNIEXPORT jint JNICALL Java_com_codip_seed_cbcplugin_SEEDCBC_seedCBCInit(JNIEnv* env, jobject thiz, jbyteArray userKey, jintArray seedKey);
JNIEXPORT void JNICALL Java_com_codip_seed_cbcplugin_SEEDCBC_encryptBlock(JNIEnv* env, jobject thiz, jbyteArray In, jbyteArray Out, jint out_index, jintArray seed_key_);
JNIEXPORT jint JNICALL Java_com_codip_seed_cbcplugin_SEEDCBC_internalSeedCBCProcessEnc(JNIEnv* env, jobject thiz, jint enc, jbyteArray seedKey, jbyteArray ivec, jbyteArray cbc_buffer, jintArray buffer_length, jbyteArray inputText, jint inputOffset, jint inputTextLen, jbyteArray outputText, jint outputOffset);
JNIEXPORT jint JNICALL Java_com_codip_seed_cbcplugin_SEEDCBC_internalSeedCBCProcessDec(JNIEnv* env, jobject thiz, jint enc, jbyteArray seedKey, jbyteArray ivec, jbyteArray cbc_buffer, jintArray buffer_length, jbyteArray cbc_last_block, jintArray last_block_flag, jbyteArray inputText, jint inputOffset, jint inputTextLen, jbyteArray outputText, jint outputOffset);
JNIEXPORT jint JNICALL Java_com_codip_seed_cbcplugin_SEEDCBC_internalSeedProcessBlocks(JNIEnv* env, jobject thiz, jintArray seedKey, jbyteArray ivec, jbyteArray cbc_buffer, jbyteArray outputText, jint outputTextLen);
#ifdef  __cplusplus
}
#endif

#endif /* HEADER_SEED_H */
