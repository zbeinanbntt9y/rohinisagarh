2010/10/09 Android Hackathon in Fukuoka vol.2

�g���ݔǁH�̐��ʕ��B

������
BeagleBoard(����ł͂Ȃ�����)��USB�V���A������f�[�^�𑗐M���邾���B
/dev/ttyUSB0 �Œ�ɂ��Ă���B
���M��́AJuJu����{�[�h�̗\��B
���̂��킩��Ȃ������̂ŁA�uJuJu�����Arduino Mega�v�̓��������Ƃ����B
�܂��AArduino�ł͂Ȃ��̂����A�s�����݊��Ƃ������Ƃ������̂ŁB


���r���h���@
Android SDK���ƁAAndroid NDK�����K�v�B
JNI������̂ŁA�r���h���ɂ͐��ndk-build���������悢�B

ant���g����悤�ɂ��Ă���B
�uant install�v�Ƃ���ƁA�^�[�Q�b�g�ւ̃A�b�v���[�h�����Ă����̂ŁA����ɕ֗��B


�����ӓ_
/dev/ttyUSB0�̓A�N�Z�X����0600�Ȃ̂ŁA
	chmod 0666 /dev/ttyUSB0
�ȂǂƂ��Ȃ��Ă͓��삵�Ȃ��B
�������A�}���������тɁA�d�����N�����邽�тɂ����Ȃ�B
����͏����I�ɉ��Ƃ��������Ƃ��낾���APlatform�Ɏ������K�v������B

