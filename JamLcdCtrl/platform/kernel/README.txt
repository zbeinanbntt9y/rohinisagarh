uImage
	Century�Ђ�USB-LCD���j�^���g����͂�

boot.scr
	BeagleBoard�ɑ}����SD�J�[�h�̑�3�p�[�e�B�V��������N������X�N���v�g

boot.cmd
cmd2scr.sh
	uboot��mkimage��boot.scr�����X�N���v�g(linux)


�X�N���v�g����N������Ƃ��̖���
	mmc init; fatload mmc 0 0x80200000 boot.scr; source 0x80200000
